import PropTypes from 'prop-types';
import SavePicksButton from "../Buttons/SavePicksButton";
import Box from '@mui/material/Box'
import { StyledTableRow } from "../../styles/SharedStyles";
import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material";
import PickSelect from "../PickSelect/PickSelect";

const PickSelections = ({ picks, setPicks }) => {

  return (
    <Box width={'100%'} display={'flex'} flexDirection={'column'} alignItems={'center'}>
      <SavePicksButton updatedPicks={picks} width={250} />
      <TableContainer component={Paper}>
        <Table size='small' sx={{ width: '100%' }}>
          <TableHead>
            <TableRow>
              <TableCell>Week</TableCell>
              <TableCell>1 pt</TableCell>
              <TableCell>3 pts</TableCell>
              <TableCell>5 pts</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {Array.from({ length: 18 }, (_, i) => (
              <StyledTableRow key={i}>
                <TableCell>{i + 1}</TableCell>
                <TableCell>
                  <PickSelect week={i + 1} picks={picks} setPicks={setPicks} value={1} />
                </TableCell>
                <TableCell>
                  <PickSelect week={i + 1} picks={picks} setPicks={setPicks} value={3} />
                </TableCell>
                <TableCell>
                  <PickSelect week={i + 1} picks={picks} setPicks={setPicks} value={5} />
                </TableCell>
              </StyledTableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <SavePicksButton updatedPicks={picks} width={250} />
    </Box>
  )
}

PickSelections.propTypes = {
  picks: PropTypes.array,
  setPicks: PropTypes.func
}

export default PickSelections;