// Libraries
import PropTypes from 'prop-types';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import { useNavigate } from 'react-router-dom';

const LeagueItem = ({ league }) => {
    // Hooks
    const navigate = useNavigate();

    // Functions
    const leagueClick = (league) => {
        navigate(`/league-detail/${league.inviteCode}`);
    };

    return (
        <Box
            component={Paper}
            elevation={20}
            onClick={() => leagueClick(league)}
            sx={{
                height: '2em',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                mt: 2,
                mb: 2,
                "&:hover": {
                    cursor: 'pointer',
                },
            }}
        >
            {league.leagueName}
        </Box>
    );
};

LeagueItem.propTypes = {
    league: PropTypes.object,
};

export default LeagueItem;
