import {Box} from "@mui/material";
import PickSelect from "../PickSelect/PickSelect";
import PropTypes from 'prop-types';

const WeeklyPicks = ({picks, week, setPicks, competitors}) => {


    return (
        <Box display={'flex'} flexDirection={'row'}>
            <PickSelect picks={picks} week={week} setPicks={setPicks} value={1} competitors={competitors}/>
            <PickSelect picks={picks} week={week} setPicks={setPicks} value={3} competitors={competitors}/>
            <PickSelect picks={picks} week={week} setPicks={setPicks} value={5} competitors={competitors}/>
        </Box>
    )
}

WeeklyPicks.propTypes = {
    picks: PropTypes.array,
    week: PropTypes.number,
    setPicks: PropTypes.func,
    competitors: PropTypes.array
}

export default WeeklyPicks;