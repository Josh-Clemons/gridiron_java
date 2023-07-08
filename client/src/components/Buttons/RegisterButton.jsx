import {Button} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import {useNavigate} from 'react-router-dom';
import PropTypes from 'prop-types';

const RegisterButton = ({width}) => {
    const navigate = useNavigate();
    return (
        <Button variant="contained" color="info" onClick={() => navigate('/register')}
                sx={{width: width, m: 1, borderWidth: 2, '&:hover': {borderWidth: '2px'}}}>Register<SearchIcon
            sx={{ml: 2}}/></Button>
    )
}
RegisterButton.propTypes = {
    width: PropTypes.number
}
export default RegisterButton;