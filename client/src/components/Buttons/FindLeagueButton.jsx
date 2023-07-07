import {Button} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import React from 'react';
import {useNavigate} from 'react-router-dom';
import PropTypes from 'prop-types';

const FindLeagueButton = ({width}) => {
    const navigate = useNavigate();
    return (
        <Button variant="outlined" color="success" onClick={() => navigate('/find-league')}
                sx={{width: width, m: 1, borderWidth: 2, '&:hover': {borderWidth: '2px'}}}>Find A League<SearchIcon
            sx={{ml: 2}}/></Button>
    )
}
FindLeagueButton.propTypes = {
    width: PropTypes.number
}
export default FindLeagueButton;