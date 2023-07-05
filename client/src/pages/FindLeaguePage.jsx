import {useQuery} from "react-query";
import {fetchAvailableLeagues} from "../utils/api.js";
import {Box, Typography} from "@mui/material";
import {useContext} from "react";
import {UserContext} from "../contexts/UserContext.jsx";
import {useNavigate} from "react-router-dom";


const FindLeaguePage = () => {
    const navigate = useNavigate();
    const {user} = useContext(UserContext);

    const {data: availableLeagues, error, isLoading, isError}
        = useQuery(['availableLeagues', {accessToken: user.accessToken}], fetchAvailableLeagues);


    const leagueClick = (league) => {
        navigate(`/league-detail/${league.id}`)
    };

    return (
        <Box
            sx={{

                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                padding: "10px",
                textAlign: "center",
                maxWidth: 600,
                margin: 'auto'
            }}
        >
            <Typography variant="h4" color='primary.main'>Find a New League</Typography>
            <Typography variant='h6' color='primary.main' sx={{textDecoration: 'underline'}}>Public
                Leagues:</Typography>
            <Box height={"75vh"} width={"92%"}>
                {isLoading && <div>Loading....</div>}
                {isError && <div>Error retrieving available leagues: {error}</div>}
                {availableLeagues && availableLeagues.length > 0 ?
                    availableLeagues?.map(league => {
                        return (
                            <div key={league.id} onClick={() => {
                                leagueClick(league)
                            }}>
                                {league.id}
                            </div>
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