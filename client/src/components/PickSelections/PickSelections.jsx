import { useContext } from "react";
import WeeklyPicks from "../WeeklyPicks/WeeklyPicks";
import { CompetitorContext } from "../../contexts/CompetitorContext";
import PropTypes from 'prop-types';

const PickSelections = ({picks, setPicks}) => {
  const { competitors } = useContext(CompetitorContext);

  return (
    <>
      {Array.from({ length: 18 }, (_, i) => (
        <div key={i}>
          <h3>Week: {i + 1}</h3>
          <WeeklyPicks competitors={competitors} week={i + 1} picks={picks} setPicks={setPicks} />
        </div>
      ))}
    </>
  )
}

PickSelections.propTypes = {
  picks: PropTypes.array,
  setPicks: PropTypes.func
}

export default PickSelections;