export const setFilterAccount = (account) => ({
    type: 'SET_FILTER_ACCOUNT',
    payload: account,
})

export const getFilterAccount = () => ({
    type: 'GET_FILTER_ACCOUNT',
})

export const setStartDate = (startDate) => ({
    type: 'SET_START_DATE',
    payload: startDate,
})

export const setEndDate = (endDate) => ({
    type: 'SET_END_DATE',
    payload: endDate,
})

export const setErrorList = (errorList)=>({
    type: 'SET_ERRORLIST',
    payload: errorList,
})