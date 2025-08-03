import "./Categories.css"
import {useEffect, useState} from "react"
import Header from "../../components/header/Header"
import PopUpContainer from "../../components/pop-up/PopUpContainer"
import axios from "axios"
import CategoryList from "../../components/categorylist/CategoryList"
import {FiPlus} from "react-icons/fi"
import CategoryModal from "../../components/category-modal/CategoryModal"
import BACKEND_ADDRESS_URL from "../../services/backendAddress"

const Caregories = () => {
    const [user, setUser] = useState()
    const [error, setError] = useState([])
    const [categories, setCategories] = useState([])
    const [category, setCategory] = useState()
    const [selected, setSelected] = useState([])
    const [showModal, setShowModal] = useState(false)

    useEffect(() => {
        setUser(localStorage.getItem("username"))
        getAllCategories()
    }, [])

    function getAllCategories() {
        const url = `${BACKEND_ADDRESS_URL}/category`
        const token = localStorage.getItem("jwt")
        axios.get(url, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(data => setCategories(data))
            .catch(error => console.log(error.response.data[0]))
    }

    function addCategory(category) {
        const url = `${BACKEND_ADDRESS_URL}/category`
        const token = localStorage.getItem("jwt")
        axios.post(url, {
            ...category,
        }, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(()=>getAllCategories())
            .catch(error => console.log(error))
    }

    return (
        <div>
            {error && <PopUpContainer messenges={error}/>}
            {showModal && <CategoryModal category={category} isOpen={() => setShowModal(false)} addCategory={addCategory} />}
            <Header username={user} site={"categories"}/>
            <div className={"categories-content"}>
                <div className={"head"}>
                    <div>
                        Categories
                    </div>
                    <div className={"buttons"}>
                        <div className={"add-buttons"}>
                            <button onClick={() => {
                                setShowModal(true)
                                setCategory(null)
                            }}><FiPlus/> Add new
                            </button>
                        </div>
                        <div className={"delete-button"}>
                            {selected.length > 0 &&
                                <button onClick={() => {/*TODO abaendern und delete einbauen*/
                                }}>Delete ({selected.length})</button>}
                        </div>
                    </div>
                </div>
                <div className={"table-content"}>
                    <CategoryList categories={categories}/>
                </div>
            </div>
        </div>
    )
}

export default Caregories