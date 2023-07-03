import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';

import { Box } from '@mui/material';
import { UserContext } from '../contexts/UserContext';

const LoginPage = () => {
    const {signIn} = useContext(UserContext);


    return (
        <Box>   
           Hello World
        </Box>
    )
}

export default LoginPage;