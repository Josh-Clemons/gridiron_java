import { Button } from '@mui/material';
import DoneAllIcon from '@mui/icons-material/DoneAll';
import PropTypes from 'prop-types';
import useSavePicks from '../../hooks/useSavePicks';

const SavePicksButton = ({ width, updatedPicks }) => {
  const savePicksMutation = useSavePicks();

  const savePicks = (updatedPicks) => {
    savePicksMutation.mutate(updatedPicks);
  }

  return (
    <>
      <Button variant='outlined'
        size='large'
        color='success'
        onClick={() => savePicks(updatedPicks)}
        sx={{ width: width, mt: 2, mb: 2, borderWidth: '2px', '&:hover': { borderWidth: '2px' } }}>
        Save Picks<DoneAllIcon sx={{ ml: 2 }} />
      </Button>
      <div>{JSON.stringify(updatedPicks)}</div>
    </>
  )
}
SavePicksButton.propTypes = {
  width: PropTypes.number,
  size: PropTypes.string,
  updatedPicks: PropTypes.array
}
export default SavePicksButton;