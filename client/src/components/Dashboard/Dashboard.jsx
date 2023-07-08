import {useContext, useEffect, useState} from "react";
import {UserContext} from "../../contexts/UserContext";
import {Box, Paper, Typography} from "@mui/material";
import useLeaguesByUser from '../../hooks/useLeaguesByUser';
import FindLeagueButton from "../Buttons/FindLeagueButton.jsx";
import CreateLeagueButton from "../Buttons/CreateLeagueButton.jsx";
import LeagueItem from "../LeagueItem/LeagueItem";


export const Dashboard = () => {

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
                                    <LeagueItem key={league.id} league={league}/>
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
                                    <LeagueItem key={league.id} league={league}/>
                                )
                            })}
                        </Box>
                    </>
                    :
                    null
                }
                <FindLeagueButton width={250}/>
                <CreateLeagueButton width={250}/>
            </Box>
        </Box>
    )
}
