// External library imports
import {useContext, useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {useQuery} from "react-query";
import {Box, ButtonGroup, Button} from '@mui/material';

// Internal module imports
import {UserContext} from "../contexts/UserContext";
import {fetchLeagueDetails, inviteNewMember} from "../utils/api.js";
import useLeaguePicks from "../hooks/useLeaguePicks";
import LeagueStandings from "../components/LeagueStandings/LeagueStandings.jsx";
import LeagueDetails from "../components/LeagueDetails/LeagueDetails";
import PickSelections from "../components/PickSelections/PickSelections";
import LeagueOverview from "../components/LeagueOverview/LeagueOverview";
import {ThreeCircles} from "react-loader-spinner";
import axios from 'axios';


const LeagueDetailsPage = () => {
    // Parameters
    const {inviteCode} = useParams();

    // Context
    const {user} = useContext(UserContext);

    // Hooks
    const {data: picks, isLoadingPicks} = useLeaguePicks(inviteCode);
    const {data: leagueDetails} = useQuery(
        ['leagueDetails', {accessToken: user.accessToken, inviteCode}],
        fetchLeagueDetails,
        {refetchOnWindowFocus: false}
    );

    // State Variables
    const [myPicks, setMyPicks] = useState();
    const [leagueScores, setLeagueScores] = useState([]);
    const [isLeagueOwner, setIsLeagueOwner] = useState(false);
    const [isLeagueMember, setIsLeagueMember] = useState(false);
    const [viewState, setViewState] = useState('standings');

    useEffect(() => {
        let leagueScores = [];
        const usernames = Array.from(
            new Set(
                picks?.map((pick) => pick.owner.username)
            )
        );
        for (let username of usernames) {
            const tempUserPicks = picks.filter(pick => pick.owner.username === username)
            let score = 0;
            for (let pick of tempUserPicks) {
                if (pick?.competitor?.winner) score = score + pick.value;
            }
            leagueScores.push({username: username, score: score});
        }

        // Sorting the leagueScores array based on the score
        leagueScores.sort((a, b) => b.score - a.score);

        setLeagueScores(leagueScores);

        setMyPicks(picks?.filter(pick => pick.owner.id === user.id).map(pick => {
            return ({
                id: pick.id,
                ownerId: user.id,
                team: pick.competitor ? pick.competitor.team.abbreviation : "",
                event: pick.competitor ? pick.competitor.eventId : "",
                leagueId: pick.league?.id,
                value: pick.value,
                week: pick.week,
                startDate: pick.competitor ? pick.competitor.startDate : "",
                isWinner: pick.competitor ? pick.competitor.winner : false
            })
        }));

        if (picks?.length > 0) {
            if (picks[0].league.leagueOwner === user.username) {
                setIsLeagueOwner(true);
            } else if (picks?.some(pick => pick.owner.id === user.id)) {
                setIsLeagueMember(true);
            } else {
                setIsLeagueOwner(false);
                setIsLeagueMember(false);
            }
        }
    }, [picks, user.id, user.username])


    if (isLoadingPicks) {
        return (
            <Box sx={{display: 'flex', height: '100vh', mt: 5}}>
                <ThreeCircles
                    type="ThreeDots"
                    color="#5BC0BE"
                    height={100}
                    width={100}
                />
            </Box>
        )
    }

    return (
        <Box minHeight={'100vh'} m={.5} pb={15} display={'flex'} flexDirection={'column'} alignItems={'center'}>
            <button onClick={() => inviteNewMember("mrjoshc@gmail.com", inviteCode, user.accessToken)}>send test email
            </button>
            <LeagueDetails isMember={isLeagueMember} isOwner={isLeagueOwner} leagueDetails={leagueDetails}/>
            {/*Button group is for selecting the component being rendered on league details page*/}
            <ButtonGroup
                variant="text"
                aria-label="text button group"
                sx={{
                    display: 'flex',
                    flexDirection: 'row',
                    justifyContent: 'center',
                    width: '90%',
                    maxWidth: 600,
                    mt: 2,
                    mb: 2,
                }}
            >
                <Button onClick={() => setViewState('standings')} sx={{width: '30%'}}>Standings</Button>
                {(isLeagueMember || isLeagueOwner) &&
                    <Button onClick={() => setViewState('Picks')} sx={{width: '30%'}}>Picks</Button>}
                {(isLeagueMember || isLeagueOwner) &&
                    <Button onClick={() => setViewState('overview')} sx={{width: '30%'}}>Overview</Button>}

            </ButtonGroup>

            <Box p={1} width={'100%'} maxWidth={'700px'} display={'flex'} flexDirection={'column'}
                 alignItems={'center'}>
                {/* Shows a different component contingent on the choice the user makes, starts at league standings */}
                {viewState === 'standings' && <LeagueStandings leagueScores={leagueScores}/>}
                {viewState === 'Picks' && <PickSelections picks={myPicks} setPicks={setMyPicks}/>}
                {/*{(viewState === 'Picks' && isLeagueOwner) && <PicksCommissioner />}*/}
                {viewState === 'overview' && <LeagueOverview picks={picks}/>}
            </Box>
        </Box>
    )
}

export default LeagueDetailsPage;