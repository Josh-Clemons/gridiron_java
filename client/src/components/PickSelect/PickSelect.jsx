import { NativeSelect, FormControl } from '@mui/material';
import PropTypes from 'prop-types';
import { getSelectOptions } from '../../utils/PickUtils';
import { useContext, useEffect, useState } from 'react';
import { CompetitorContext } from '../../contexts/CompetitorContext';
import { errorAlert } from "../../utils/ToastAlerts.js";

const PickSelect = ({ picks, week, value, setPicks }) => {
  const { competitors } = useContext(CompetitorContext);
  const [selectOptions, setSelectOptions] = useState(null);
  const [pick, setPick] = useState(null);
  const [selectedOption, setSelectedOption] = useState('');

  useEffect(() => {
    if (picks != null) {
      setPick(picks?.find(pick => pick.value === value && pick.week === week));
    }
  }, [picks, value, week])

  useEffect(() => {
    setSelectOptions(getSelectOptions(competitors, week, (pick && pick.competitor && pick.competitor.team) ? pick.competitor.team.abbreviation : ""));
    setSelectedOption(pick?.competitor?.team?.abbreviation || '');
  }, [competitors, week, pick])

  const handleChange = event => {
    const selectedOption = event.target.value;
    const newCompetitor = competitors.find(c => c.week === week && c.team.abbreviation === selectedOption);

    // pick validations
    if (validateTeamForValue(selectedOption)) {
      errorAlert(`Can't use ${selectedOption} twice for value ${value}`);
      setSelectedOption(pick?.competitor?.team?.abbreviation || '');
      return;
    }

    // after passing validation
    setSelectedOption(selectedOption);

    setPicks(prev => (
      prev.map(pick => {
        // if the pick matches the current week and value, update the competitor
        if (pick.week === week && pick.value === value) {
          return {
            ...pick,
            competitor: newCompetitor,
          };
        }
        // otherwise, return the pick as is
        return pick;
      })
    ));
  };

  const isPickStartDatePast = () => {
    // if (pick && pick.competitor && pick.competitor.startDate) {
    //   const pickDate = new Date(pick.competitor.startDate);
    //   const currentDate = new Date();
    //   return pickDate < currentDate;
    // }
    return false;
  }

  const validateTeamForValue = (selectedTeam) => {
    return picks.some(pick =>
      pick.value === value && pick.competitor && pick.competitor.team && pick.competitor.team.abbreviation === selectedTeam
    );
  }

  return (
    <FormControl variant="standard" fullWidth>
      <NativeSelect
        value={selectedOption}
        disabled={isPickStartDatePast()}
        onChange={handleChange}
        inputProps={{
          name: 'team',
          id: `pick${week}${value}`,
        }}
      >
        {selectOptions && selectOptions.map((option, i) => (
          <option key={i} value={option.value}>
            {option.label}
          </option>
        ))}
      </NativeSelect>
    </FormControl>
  );

};

PickSelect.propTypes = {
  picks: PropTypes.array,
  week: PropTypes.number,
  value: PropTypes.number,
  setPicks: PropTypes.func,
  competitors: PropTypes.array
};

export default PickSelect;