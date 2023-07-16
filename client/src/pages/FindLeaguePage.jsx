// Libraries
import { useQuery } from "react-query";
import { useContext } from "react";
import { Box, Typography } from "@mui/material";

// Contexts
import { UserContext } from "../contexts/UserContext.jsx";

// Hooks
import useScrollToTop from "../hooks/useScrollToTop.js";

// API
import { fetchAvailableLeagues } from "../utils/api.js";

// Components
import CreateLeagueButton from "../components/Buttons/CreateLeagueButton.jsx";
import LeagueItem from "../components/LeagueItem/LeagueItem.jsx";
import JoinLeagueByCodeButton from "../components/Buttons/JoinLeagueByCodeButton.jsx";

const FindLeaguePage = () => {
    // Hooks
    useScrollToTop();
    const { user } = useContext(UserContext);

    // Query
    const { data: availableLeagues, error, isLoading, isError }
        = useQuery(['availableLeagues', { accessToken: user.accessToken }], fetchAvailableLeagues);

    return (
        <Box
            minHeight={"100vh"}
            sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "flex-start",
                padding: "10px",
                textAlign: "center",
                maxWidth: 600,
                margin: 'auto'
            }}
        >
            <Typography variant="h4" color='primary.main'>Find a New League</Typography>
            <CreateLeagueButton width={250}/>
            <JoinLeagueByCodeButton/>
            <Typography variant='h6' color='primary.main' sx={{textDecoration: 'underline'}}>Public
                Leagues:</Typography>
            <Box width={"92%"}>
                {isLoading && <div>Loading....</div>}
                {isError && <div>Error retrieving available leagues: {error}</div>}
                {availableLeagues && availableLeagues.length > 0 ?
                    availableLeagues?.map(league => {
                        return (
                            <LeagueItem key={league.id} league={league}/>
                        )
                    })
                    :
                    <Typography>Join or Create a league!</Typography>
                }
            </Box>
        </Box>
    )


}

export default FindLeaguePage;