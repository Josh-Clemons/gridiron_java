import { Box } from "@mui/material";
import PickSelect from "../PickSelect/PickSelect";
import PropTypes from 'prop-types';

const WeeklyPicks = ({picks, week, setPicks}) => {



  return(
    <Box display={'flex'} flexDirection={'row'}>
      <PickSelect picks={picks} week={week} setPicks={setPicks} value={1} />
      <PickSelect picks={picks} week={week} setPicks={setPicks} value={3} />
      <PickSelect picks={picks} week={week} setPicks={setPicks} value={5} />
    </Box>
  )
}

WeeklyPicks.propTypes = {
  picks: PropTypes.array,
  week: PropTypes.number,
  setPicks: PropTypes.func
}

export default WeeklyPicks;