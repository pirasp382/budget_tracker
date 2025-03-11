import "./CategoryModal.css"
import {useState} from "react"
import {AiOutlineCloseCircle} from "react-icons/ai"
import ColorPicker from "../colorpicker/ColorPicker"
import IconPicker from "../icon-picker/IconPicker"

const CategoryModal = ({category, isOpen, addCategory, saveEdits}) => {

    const [title, setTitle] = useState("")
    const [description, setDescription] = useState()
    const [color, setColor] = useState()
    const [icon, setIcon] = useState()

    function handleSave() {
        const id = category.id
        const new_category = {id, title, description, color, icon}
        saveEdits(new_category)
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        const new_category = {title, description, color, icon}
        addCategory(new_category)
    }

    return (
        <div className={"category-form"}>
            <div className={"upper"}>
                <div className={"category-title"}>
                    {category ? <p className={"title"}>Edit Category</p> :
                        <p className={"title"}>New Category</p>}
                    {category ? <p className={"sub-title"}>Edit the Category</p> :
                        <p className={"sub-title"}>Add a new Category</p>}
                </div>
                <div className={"close"} onClick={isOpen}><AiOutlineCloseCircle/></div>
            </div>
            <form className={"form"} onSubmit={handleSubmit}>
                <label>Title</label>
                <input type={"text"} value={title} required={true}
                       onChange={(event) => setTitle(event.target.value)}/>
                <label>
                    Notes: <br/>
                    <textarea type="textarea" placeholder={"Optionales Notes"} value={description}
                              onChange={(e) => setDescription(e.target.value)}/>
                </label>

                <ColorPicker onChange={setColor}/>

                <label>Icon: <br/>
                    <input type={"icon"}/></label>


                {category ? <button type="button" onClick={handleSave}>Save changes</button> :
                    <button type="submit">Add an account</button>}
            </form>
        </div>
    )
}

export default CategoryModal