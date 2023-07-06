import {useContext, useEffect, useState} from "react";
import {UserContext} from "../../contexts/UserContext";
import {Box, Paper, Typography} from "@mui/material";
import useLeaguesByUser from '../../hooks/useLeaguesByUser';
import {useNavigate} from "react-router-dom";


export const Dashboard = () => {
    const navigate = useNavigate();

    const {user} = useContext(UserContext);
    const {data: leagues, isLoading, isError, error} = useLeaguesByUser(user);

    const [myLeagues, setMyLeagues] = useState(null);
    const [othersLeagues, setOthersLeagues] = useState(null);

    useEffect(() => {
        if (leagues) {
            const myLeaguesData = leagues.filter(league => league.leagueOwner === user.username);
            const othersLeaguesData = leagues.filter(league => league.leagueOwner !== user.username);
            setMyLeagues(myLeaguesData);
            setOthersLeagues(othersLeaguesData);
        }
    }, [leagues, user.username]);

    // redirects to league detail page when a user clicks on a league item
    const leagueClick = (league) => {
        navigate(`/league-detail/${league.id}`)
    };

    return (

        <Box
            sx={{maxWidth: 600, display: 'flex', flexDirection: 'column', margin: 'auto', alignItems: 'center'}}
        >
            {isLoading && <h1>hold up</h1>}
            {isError && <h1>shit is no bueno: {error.message}</h1>}
            <Typography sx={{m: 2}} variant="h4">Dashboard</Typography>
            <Box
                width={"100%"}
                height={"75vh"}
                component={Paper}
                borderRadius={4}
                p={3}
                mt={4}
                sx={{
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "left",
                }}
            >
                {myLeagues ?
                    <>
                        <Typography variant='h6'>Leagues I Manage:</Typography>
                        <Box sx={{mb: 2}}>
                            {myLeagues.map(league => {
                                return (
                                    <Box onClick={() => {
                                        leagueClick(league)
                                    }} key={league.id}>
                                        <h2>{league.leagueName}</h2>
                                    </Box>
                                )
                            })}
                        </Box>
                    </>
                    :
                    null
                }

                {othersLeagues ?
                    <>
                        <Typography variant='h6'>Leagues I am in:</Typography>
                        <Box mb={6}>
                            {othersLeagues.map(league => {
                                return (
                                    <Box onClick={() => {
                                        leagueClick(league)
                                    }} key={league.id}>
                                        <h2>{league.leagueName}</h2>
                                    </Box>
                                )
                            })}
                        </Box>
                    </>
                    :
                    null
                }
            </Box>
        </Box>
    )
}
