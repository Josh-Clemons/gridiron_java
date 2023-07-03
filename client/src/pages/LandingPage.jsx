import { useNavigate } from 'react-router-dom';

import { Box, Button, Typography, Stack } from '@mui/material';
import footballPlayerShadow from './Images/football_player_shadow.jpeg';

const LandingPage = () => {
    const navigate = useNavigate();

    const logout = () => {
        console.log("logging out... not!!");
        navigate('/login');
    }


    return (
        <Box
            className='landingPageDiv'
            sx={{
                backgroundImage: `url(${footballPlayerShadow})`,
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
                <Button variant="contained" onClick={logout}>Logout</Button>
                {/* button options change if user is logged in */}
                {/* {!store.user.id
                    ?
                    <>
                        <Button variant="contained" color="success" onClick={() => navigate('/login')} sx={{ width: "250px" }}>Login<LoginIcon sx={{ ml: 2 }} /></Button>
                        <Button variant="contained" color="info" onClick={() => navigate('/register')} sx={{ width: "250px" }}>Register<HowToRegIcon sx={{ ml: 2 }} /></Button>
                    </>
                    :
                    <>
                        <Button variant="contained" color="success" onClick={() => navigate('/dashboard')} sx={{ width: "250px" }}>Dashboard<GridViewIcon sx={{ ml: 2 }} /></Button>
                        <Button variant="contained" color="error" onClick={logout} sx={{ width: "250px" }}>Logout<LogoutIcon sx={{ ml: 2 }} /></Button>
                    </>
                }
                <ModalRules size={'medium'} variant={'contained'} width={'250px'} /> */}
            </Stack>
        </Box>
    )
}

export default LandingPage;