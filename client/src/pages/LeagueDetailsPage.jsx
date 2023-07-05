import {useContext, useEffect, useState} from "react";
import useLeaguePicks from "../hooks/useLeaguePicks";
import {useParams} from "react-router-dom";
import {UserContext} from "../contexts/UserContext";
import WeeklyPicks from "../components/WeeklyPicks/WeeklyPicks";
import useSavePicks from "../hooks/useSavePicks.js";
import {useQuery} from "react-query";
import {fetchAvailableLeagues, fetchLeagueDetails} from "../utils/api.js";

const LeagueDetailsPage = () => {

    const {leagueId} = useParams()
    const {user} = useContext(UserContext);

    const {data: picks, isLoading, isError, error} = useLeaguePicks(leagueId);
    const {data: leagueDetails}
        = useQuery(['leagueDetails', {accessToken: user.accessToken, leagueId: leagueId}], fetchLeagueDetails);
    const [myPicks, setMyPicks] = useState();
    const savePicksMutation = useSavePicks();


    useEffect(() => {
        setMyPicks(picks?.filter(pick => pick.owner.id === user.id).map(pick => {
            return ({
                id: pick.id,
                ownerId: user.id,
                team: pick.competitor ? pick.competitor.team.abbreviation : "",
                leagueId: pick.league?.id,
                value: pick.value,
                week: pick.week
            })
        }));
    }, [picks, user.id])

    const savePicks = (picks) => {
        savePicksMutation.mutate(picks);
    };

    return (
        <>
            <h2>LeagueId: {JSON.stringify(leagueId)}</h2>
            <h2>Owner: {picks && picks[0] && JSON.stringify(picks[0].league.leagueOwner)}</h2>
            <button onClick={() => savePicks(myPicks)}>Save</button>
            {/* WeeklyPicks components for each week */}
            {Array.from({length: 18}, (_, i) => (
                <div key={i}>
                    <h3>Week: {i + 1}</h3>
                    <WeeklyPicks week={i + 1} picks={myPicks} setPicks={setMyPicks}/>
                </div>
            ))}
            <div>League Details: {JSON.stringify(leagueDetails)}</div>
        </>
    )
}

export default LeagueDetailsPage;