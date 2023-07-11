import {NativeSelect, FormControl} from '@mui/material';
import PropTypes from 'prop-types';
import {getSelectOptions} from '../../utils/PickUtils';
import {useContext, useEffect, useState} from 'react';
import {CompetitorContext} from '../../contexts/CompetitorContext';
import {errorAlert} from "../../utils/ToastAlerts.js";

const PickSelect = ({picks, week, value, setPicks}) => {
    const {competitors} = useContext(CompetitorContext);
    const [selectOptions, setSelectOptions] = useState(null);
    const [pick, setPick] = useState(null);

    useEffect(() => {
        if (picks != null) {
            setPick(picks?.find(pick => pick.value === value && pick.week === week));
        }
    }, [picks, value, week])

    useEffect(() => {
        setSelectOptions(getSelectOptions(competitors, week, pick?.team))
    }, [competitors, week, pick])

    const handleChange = event => {
        const selectedOption = event.target.value;

        // TODO data validation is going to take more work
        if (validateEventId(selectedOption)) {
            errorAlert("Can't have two teams from the same game in a given week");
            return;
        }

        setPicks(prev => (
            prev.map(pick => {
                // if the pick matches the current week and value, update the team
                if (pick.week === week && pick.value === value) {
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

    const isPickStartDatePast = () => {
        if (pick && pick.startDate) {
            const pickDate = new Date(pick.startDate);
            const currentDate = new Date();

            return pickDate < currentDate;
        }
    }

    const validateEventId = (selectedEventId) => {
        return picks.some(pick =>
            pick.week === week && pick.eventId === selectedEventId
        );
    }

    const validateTeamForValue = (selectedTeam) => {
        return picks.some(pick =>
            pick.value === value && pick.team === selectedTeam
        );
    }

    const ValidateTeamForWeek = (selectedTeam) => {
        return picks.some(pick =>
            pick.week === week && pick.team === selectedTeam
        );
    }


    return (
        <FormControl variant="standard" fullWidth>
            <NativeSelect
                value={pick?.team}
                disabled={isPickStartDatePast()}
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
    setPicks: PropTypes.func,
    competitors: PropTypes.array
};

export default PickSelect;
