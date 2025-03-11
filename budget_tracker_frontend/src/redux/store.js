import {configureStore} from "@reduxjs/toolkit"
import {accounts_operations, errorMessages_operations} from "./reducer"

export default configureStore({
    reducer: {
        accounts: accounts_operations,
        errorLists: errorMessages_operations
    },
})