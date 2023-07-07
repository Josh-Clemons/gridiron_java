import {Button} from '@mui/material';
import CreateIcon from '@mui/icons-material/Create';
import React from 'react';
import {useNavigate} from 'react-router-dom';
import PropTypes from 'prop-types';

const CreateLeagueButton = ({width}) => {
    const navigate = useNavigate();
    return (
        <Button variant="outlined" color="warning" onClick={() => navigate('/create')}
                sx={{width: width, m: 1, borderWidth: 2, '&:hover': {borderWidth: '2px'}}}>Create A League<CreateIcon
            sx={{ml: 2}}/></Button>
    )
}

CreateLeagueButton.propTypes = {
    width: PropTypes.number
}

export default CreateLeagueButton;