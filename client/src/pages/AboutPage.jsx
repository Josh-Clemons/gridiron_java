import UpdateCompetitorsButton from "../components/Buttons/UpdateCompetitorsButton.jsx";
import Box from "@mui/material/Box";
import {useContext} from "react";
import {UserContext} from "../contexts/UserContext.jsx";

const AboutPage = () => {
    const {user} = useContext(UserContext);

    return (
        <>
            <Box sx={{minHeight: '100vh'}}>
                <UpdateCompetitorsButton />
            </Box>
        </>
    )
}

export default AboutPage;