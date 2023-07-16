// Libraries
import { Box, Typography, Stack } from '@mui/material';
import { useContext } from 'react';

// Contexts
import { UserContext } from '../contexts/UserContext';

// Assets
import background from '../assets/background.jpeg';

// Components
import LoginButton from '../components/Buttons/LoginButton';
import DashboardButton from '../components/Buttons/DashboardButton';
import LogoutButton from '../components/Buttons/LogoutButton';
import RegisterButton from '../components/Buttons/RegisterButton';
import RulesButton from '../components/Buttons/RulesButton';

const LandingPage = () => {
    // Contexts
    const { user } = useContext(UserContext);

    return (
        <Box
            minHeight={'100vh'}
            className='landingPageDiv'
            sx={{
                backgroundImage: `url(${background})`,
                backgroundColor: "teal",
                backgroundSize: "cover",
                borderRadius: 2,
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                color: "white",
                padding: "10px",
                textAlign: "center"
            }}
        > <Box sx={{backgroundColor: "#0B132B", color: '#5BC0BE', p: 2, opacity: '90%', borderRadius: 4}}>
            <Typography variant='h3'>Welcome to the <Box component='span'>Grid Iron</Box></Typography>
            <Typography variant='h5' sx={{color: 'white'}}>Pick NFL Winners Against Your Friends!</Typography>
        </Box>
            <br/>
            <br/>
            <Stack spacing={1} direction="column">
                {user ?
                    <Box display={'flex'} flexDirection={'column'} alignItems={'center'}>
                        <DashboardButton width={250}/>
                        <LogoutButton width={250}/>
                        <RulesButton variant={"contained"} size={'medium'} margin={2} width={250}/>
                    </Box>
                    :
                    <Box display={'flex'} flexDirection={'column'} alignItems={'center'}>
                        <LoginButton width={250}/>
                        <RegisterButton width={250}/>
                        <RulesButton variant={"contained"} size={'medium'} margin={2} width={250}/>
                    </Box>
                }
            </Stack>
        </Box>
    )
}

export default LandingPage;