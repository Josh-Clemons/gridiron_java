import { styled } from '@mui/material';
import TableRow from '@mui/material/TableRow';

// styles for the table rows
export const StyledTableRow = styled(TableRow)(() => ({
    '&:nth-of-type(odd)': {
        backgroundColor: "#1C2541",
    },
    '&:nth-of-type(even)': {
        backgroundColor: "#242f53",
    }
}));