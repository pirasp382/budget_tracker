import React, {useState, useEffect} from "react"
import PopUp from "./PopUp"
import "./PopUp.css"

export default function PopUpContainer({messenges}) {
    const [popups, setPopups] = useState([])

    const addPopUp = (message, type = "info") => {
        const id = Date.now()
        setPopups((prev) => [...prev, {id, message, type}])
    }

    const removePopUp = (id) => {
        setPopups((prev) => prev.filter((popup) => popup.id !== id))
    }

    useEffect(() => {
        messenges.forEach((msg) => addPopUp(msg.title, "error"))
    }, [messenges])

    return (
        <div className="popup-container">
            {popups.map((popup) => (
                <PopUp
                    key={popup.id}
                    id={popup.id}
                    message={popup.message}
                    type={popup.type}
                    onClose={removePopUp}
                />
            ))}
        </div>
    )
}
