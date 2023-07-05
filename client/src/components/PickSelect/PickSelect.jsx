import { NativeSelect, FormControl, InputLabel } from '@mui/material';
import PropTypes from 'prop-types';
import { getSelectOptions } from '../../utils/PickUtils';
import { useContext, useEffect, useState } from 'react';
import { CompetitorContext } from '../../contexts/CompetitorContext';

const PickSelect = ({ picks, week, value, setPicks }) => {

  const { competitors } = useContext(CompetitorContext);
  const [selectOptions, setSelectOptions] = useState(null);

  const pick = picks?.filter(pick => pick.week === week && pick.value === value)

  useEffect(() => {
    setSelectOptions(getSelectOptions(competitors, week))
  }, [competitors, week])

  const handleChange = event => {
    const selectedOption = event.target.value;
    console.log('in Select handleChange:', selectedOption)

    setPicks(prev => (
      prev.map(pick => {
        // if the pick matches the current week and value, update the team
        if (pick.week === week && pick.value === value) {
          console.log('pick matched! updating with ' + selectedOption)
          return {
            ...pick,
            team: selectedOption,
          };
        }
        // otherwise, return the pick as is
        return pick;
      })
    ));
  };

  return (
    <FormControl variant="standard" fullWidth>
      <InputLabel variant="standard" shrink={pick && pick.team !== 'none'} htmlFor={`pick${week}${value}`}>
        Team
      </InputLabel>
      <NativeSelect
        value={pick && pick.team != '' ? pick.team : ''}
        onChange={handleChange}
        inputProps={{
          name: 'team',
          id: `pick${week}${value}`,
        }}
      >
        {selectOptions && selectOptions.map(option => (
          <option key={option.value} value={option.value}>
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
  setPicks: PropTypes.func.isRequired
};

export default PickSelect;
