import React, { useEffect, useState } from "react";
import "./PopUp.css";

export default function PopUp({ id, message, type = "info", onClose }) {
    const [progress, setProgress] = useState(100);

    useEffect(() => {
        const interval = setInterval(() => {
            setProgress((prev) => Math.max(0, prev - 2));
        }, 100);

        const timeout = setTimeout(() => {
            onClose(id);
        }, 5000);

        return () => {
            clearInterval(interval);
            clearTimeout(timeout);
        };
    }, [id, onClose]);

    return (
        <div className={`popup popup-${type}`}>
            <p className="popup-message">{message}</p>
            <div className="popup-progress-bar">
                <div
                    className="popup-progress"
                    style={{ width: `${progress}%` }}
                ></div>
            </div>
        </div>
    );
}
