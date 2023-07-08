import {Button} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import {useNavigate} from 'react-router-dom';
import PropTypes from 'prop-types';

const DashboardButton = ({width}) => {
    const navigate = useNavigate();
    return (
        <Button variant="contained" color="success" onClick={() => navigate('/dashboard')}
                sx={{width: width, m: 1, borderWidth: 2, '&:hover': {borderWidth: '2px'}}}>Dashboard<SearchIcon
            sx={{ml: 2}}/></Button>
    )
}
DashboardButton.propTypes = {
    width: PropTypes.number
}
export default DashboardButton;