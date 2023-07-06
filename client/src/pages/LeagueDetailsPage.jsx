import {useContext, useEffect, useState} from "react";
import useLeaguePicks from "../hooks/useLeaguePicks";
import {useParams} from "react-router-dom";
import {UserContext} from "../contexts/UserContext";
import WeeklyPicks from "../components/WeeklyPicks/WeeklyPicks";
import useSavePicks from "../hooks/useSavePicks.js";
import {useQuery} from "react-query";
import {fetchLeagueDetails} from "../utils/api.js";
import {CompetitorContext} from "../contexts/CompetitorContext.jsx";
import LeagueStandings from "../components/LeagueStandings/LeagueStandings.jsx";

const LeagueDetailsPage = () => {

    const {leagueId} = useParams()
    const {user} = useContext(UserContext);
    const {competitors, isLoadingCompetitors} = useContext(CompetitorContext);

    const {data: picks, isLoadingPicks, isError, error} = useLeaguePicks(leagueId);
    const {data: leagueDetails}
        = useQuery(['leagueDetails', {
        accessToken: user.accessToken,
        leagueId: leagueId
    }], fetchLeagueDetails, {refetchOnWindowFocus: false});
    const [myPicks, setMyPicks] = useState();
    const [leagueScores, setLeagueScores] = useState([]);
    const [isLeagueOwner, setIsLeagueOwner] = useState(false);
    const [isLeagueMember, setIsLeagueMember] = useState(false);

    const savePicksMutation = useSavePicks();

    useEffect(() => {
        setLeagueScores(updateLeagueScores(picks));
        setMyPicks(findMyPicks(picks));
        setUserStatus(picks);
    }, [picks, user.id])

    const updateLeagueScores = (picks) => {
        let leagueScores = [];
        let usernames = Array.from(
            new Set(
                picks?.map((pick) => pick.owner.username)
            )
        );
        for (let username of usernames) {
            let tempUserPicks = picks.filter(pick => pick.owner.username === username)
            let score = 0;
            for (let pick of tempUserPicks) {
                console.log(pick)
                if (pick?.competitor?.winner) score = score + pick.value;
            }
            leagueScores.push({username: user.username, score: score});
        }
        return leagueScores;
    }

    const setUserStatus = (picks) => {
        if (picks?.length > 0) {
            if (picks[0].league.leagueOwner === user.username) {
                setIsLeagueOwner(true);
            } else if (picks[0].owner.id === user.id) {
                setIsLeagueMember(true);
            } else {
                setIsLeagueOwner(false);
                setIsLeagueMember(false);
            }
        }
    }

    const findMyPicks = (picks) => {
        return picks?.filter(pick => pick.owner.id === user.id).map(pick => {
            return ({
                id: pick.id,
                ownerId: user.id,
                team: pick.competitor ? pick.competitor.team.abbreviation : "",
                leagueId: pick.league?.id,
                value: pick.value,
                week: pick.week
            })
        })
    }


    const savePicks = (picks) => {
        savePicksMutation.mutate(picks);
    };

    if (isLoadingPicks || isLoadingCompetitors) {
        return <div>Loading...</div>;
    }

    return (
        <>
            <h2>LeagueId: {JSON.stringify(leagueId)}</h2>
            <h2>Owner: {picks && picks[0] && leagueDetails?.leagueOwner}</h2>
            <button onClick={() => savePicks(myPicks)}>Save</button>
            <LeagueStandings leagueScores={leagueScores}/>
            {/* WeeklyPicks components for each week */}
            {Array.from({length: 18}, (_, i) => (
                <div key={i}>
                    <h3>Week: {i + 1}</h3>
                    <WeeklyPicks competitors={competitors} week={i + 1} picks={myPicks} setPicks={setMyPicks}/>
                </div>
            ))}
            <div>isMember: {JSON.stringify(isLeagueMember)}, isOwner: {JSON.stringify(isLeagueOwner)}</div>
            <div>Scores: {JSON.stringify(leagueScores)}</div>
            <div>League: {JSON.stringify(leagueDetails)}</div>
            <div>League Details: {JSON.stringify(picks)}</div>
        </>
    )
}

export default LeagueDetailsPage;