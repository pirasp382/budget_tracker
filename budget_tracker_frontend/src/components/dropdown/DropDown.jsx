import {useEffect, useState} from "react"
import "./DropDown.css"


const DropDown = ({saveSelected, selectOptions, defaultValue}) => {
    const [open, setOpen] = useState(false)
    const [value, setValue] = useState()

    const handleSelect = (value) => {
        saveSelected(value)
        setValue(value)
        setOpen(false)
    }

    useEffect(() => {
        const handleKeyDown = (event) => {
            if (event.keyCode === 27) {
                setOpen(false)
            }
        }
        window.addEventListener("keydown", handleKeyDown)
        return () => window.removeEventListener("keydown", handleKeyDown)
    }, [setOpen])

    return (
        <div className="dropdown">
            <button onClick={() => setOpen(!open)}>{value ? selectOptions[value] : defaultValue}</button>
            {open && (
                <div className="dropdown-menu">
                    {Object.entries(selectOptions).map(item => <div
                        onClick={() => handleSelect(item[0])}>{item[1]}</div>)}
                </div>
            )}
        </div>
    )
}

export default DropDown
