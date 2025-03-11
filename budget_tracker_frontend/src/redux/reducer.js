const today = () => new Date()

const initialState = {
    filter_account: "",
    errorList: [],
}

export const accounts_operations = (state = initialState, action) => {
    switch (action.type) {
        case "GET_FILTER_ACCOUNT":
            return {...state}
        case "SET_FILTER_ACCOUNT":
            return {...state, filter_account: action.payload}
    }
    return state
}

export const errorMessages_operations = (state = initialState, action) => {
    switch (action.type) {
        case "SET_ERRORLIST":
            return {...state, errorList: action.payload}
        default:
            return state
    }
}