// Libraries
import {Button} from '@mui/material';
import PropTypes from 'prop-types';
import SearchIcon from '@mui/icons-material/Search';
import {useNavigate} from 'react-router-dom';

const LoginButton = ({width}) => {
    const navigate = useNavigate();
    return (
        <Button variant="contained" color="success" onClick={() => navigate('/login')}
                sx={{width: width, m: 1, borderWidth: 2, '&:hover': {borderWidth: '2px'}}}>Login<SearchIcon
            sx={{ml: 2}}/></Button>
    )
}

LoginButton.propTypes = {
    width: PropTypes.number
}

export default LoginButton;