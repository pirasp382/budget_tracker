import {useEffect, useState} from "react"
import { ChromePicker } from "react-color";

const ColorPicker = ({ label = "Pick a color", defaultColor = "#4CAF50", onChange }) => {
    const [color, setColor] = useState(defaultColor);
    const [showPicker, setShowPicker] = useState(false);

    const handleChange = (newColor) => {
        setColor(newColor.hex);
        if (onChange) onChange(newColor.hex);
    };

    useEffect(() => {
        const handleKeyDown = (event) => {
            if (event.key === "Escape") {
                setShowPicker(false);
            }
        };

        if (showPicker) {
            window.addEventListener("keydown", handleKeyDown);
        }

        return () => {
            window.removeEventListener("keydown", handleKeyDown);
        };
    }, [showPicker]);

    return (
        <div style={{ position: "relative" }}>
            <label>{label}:</label>
            <div
                style={{
                    width: "36px", height: "36px", borderRadius: "50%",
                    backgroundColor: color, cursor: "pointer",
                    border: "2px solid #ddd"
                }}
                onClick={() => setShowPicker(!showPicker)}
            />
            {showPicker && (
                <div style={{ position: "absolute", zIndex: 2 }}>
                    <ChromePicker color={color} onChange={handleChange} />
                </div>
            )}
        </div>
    );
};

export default ColorPicker;
