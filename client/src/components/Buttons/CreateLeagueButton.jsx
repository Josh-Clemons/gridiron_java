// Libraries
import PropTypes from 'prop-types';
import { Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';

// Assets
import CreateIcon from '@mui/icons-material/Create';

const CreateLeagueButton = ({ width }) => {
    const navigate = useNavigate();

    return (
        <Button
            variant="outlined"
            color="warning"
            onClick={() => navigate('/create')}
            sx={{
                width: width,
                m: 1,
                borderWidth: 2,
                '&:hover': { borderWidth: '2px' }
            }}
        >
            Create A League
            <CreateIcon sx={{ ml: 2 }}/>
        </Button>
    );
};

CreateLeagueButton.propTypes = {
    width: PropTypes.number,
};

export default CreateLeagueButton;
