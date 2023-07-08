
import { Box, Typography, Stack } from '@mui/material';
import background from '../assets/background.jpeg';
import { useContext } from 'react';
import { UserContext } from '../contexts/UserContext';
import LoginButton from '../components/Buttons/LoginButton';
import DashboardButton from '../components/Buttons/DashboardButton';
import LogoutButton from '../components/Buttons/LogoutButton';
import RegisterButton from '../components/Buttons/RegisterButton';
import RulesButton from '../components/Buttons/RulesButton';

const LandingPage = () => {

    const { user } = useContext(UserContext);


    return (
        <Box
            className='landingPageDiv'
            sx={{
                backgroundImage: `url(${background})`,
                backgroundColor: "teal",
                backgroundSize: "cover",
                height: "80vh",
                borderRadius: 2,
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                color: "white",
                padding: "10px",
                textAlign: "center"
            }}
        >   <Box sx={{ backgroundColor: "#0B132B", color: '#5BC0BE', p: 2, opacity: '90%', borderRadius: 4 }}>
                <Typography variant='h3'>Welcome to the <Box component='span'>Grid Iron</Box></Typography>
                <Typography variant='h5' sx={{ color: 'white' }}>Pick NFL Winners Against Your Friends!</Typography>
            </Box>
            <br />
            <br />
            <Stack spacing={1} direction="column">
                {user ?
                    <>
                        <DashboardButton width={250} />
                        <LogoutButton width={250} />
                        <RulesButton variant={"contained"} size={'medium'} margin={2}  width={250} />
                    </>
                    :
                    <>
                        <LoginButton width={250} />
                        <RegisterButton width={250} />
                        <RulesButton variant={"contained"} size={'medium'} margin={2}  width={250} />
                    </>
                }
                {/* button options change if user is logged in */}
                {/*
                <ModalRules size={'medium'} variant={'contained'} width={'250px'} /> */}
            </Stack>
        </Box>
    )
}

export default LandingPage;