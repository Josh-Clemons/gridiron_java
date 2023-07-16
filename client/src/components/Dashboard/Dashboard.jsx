// Libraries
import { useContext, useEffect, useState } from "react";
import { Box, Paper, Typography } from "@mui/material";

// Contexts
import { UserContext } from "../../contexts/UserContext";

// Hooks
import useLeaguesByUser from '../../hooks/useLeaguesByUser';

// Components
import FindLeagueButton from "../Buttons/FindLeagueButton.jsx";
import CreateLeagueButton from "../Buttons/CreateLeagueButton.jsx";
import LeagueItem from "../LeagueItem/LeagueItem";

export const Dashboard = () => {
    // Contexts
    const { user } = useContext(UserContext);

    // State
    const [myLeagues, setMyLeagues] = useState([]);
    const [othersLeagues, setOthersLeagues] = useState([]);

    // Hooks
    const { data: leagues, isLoading, isError, error } = useLeaguesByUser(user);

    // Effects
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
            sx={{
                maxWidth: 600,
                display: 'flex',
                flexDirection: 'column',
                margin: 'auto',
                alignItems: 'center',
                minHeight: '100vh'
            }}
        >
            {isLoading && <h1>hold up</h1>}
            {isError && <h1>shit is no bueno: {error.message}</h1>}
            <Typography sx={{ m: 2 }} variant="h4">Dashboard</Typography>
            <Box
                overflow={'auto'}
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
                <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                    {myLeagues.length > 0 &&
                        <Box sx={{ width: '100%' }}>
                            <Typography variant='h6'>Leagues I Manage:</Typography>
                            <Box sx={{ mb: 2 }}>
                                {myLeagues.map(league => {
                                    return (
                                        <LeagueItem key={league.id} league={league} />
                                    )
                                })}
                            </Box>
                        </Box>
                    }
                    {othersLeagues.length > 0 &&
                        <Box sx={{ width: '100%' }}>
                            <Typography variant='h6'>Leagues I am in:</Typography>
                            <Box mb={6}>
                                {othersLeagues.map(league => {
                                    return (
                                        <LeagueItem key={league.id} league={league} />
                                    )
                                })}
                            </Box>
                        </Box>
                    }
                    {leagues?.length <= 4 &&
                        <>
                            <FindLeagueButton width={250} />
                            <CreateLeagueButton width={250} />
                        </>
                    }
                </Box>
            </Box>
        </Box>
    )
}
