// Libraries
import PropTypes from 'prop-types';
import { Button } from '@mui/material';
import DoneAllIcon from '@mui/icons-material/DoneAll';
import {ThreeCircles} from "react-loader-spinner";

// Hooks
import useSavePicks from '../../hooks/useSavePicks';

const SavePicksButton = ({ width, updatedPicks }) => {
  const { mutate, isLoading } = useSavePicks();

  const savePicks = (updatedPicks) => {
    mutate(updatedPicks);
  }

  return (
      <>
        {isLoading ? (
            <ThreeCircles
                color="#5BC0BE"
                height={76}
                width={70}
            />
        ) : (
            <Button variant='outlined'
                    size='large'
                    color='success'
                    onClick={() => savePicks(updatedPicks)}
                    sx={{ width: width, mt: 2, mb: 2, borderWidth: '2px', '&:hover': { borderWidth: '2px' } }}>
              Save Picks<DoneAllIcon sx={{ ml: 2 }} />
            </Button>
        )}
      </>
  )
}

SavePicksButton.propTypes = {
  width: PropTypes.number,
  size: PropTypes.string,
  updatedPicks: PropTypes.array
}

export default SavePicksButton;
