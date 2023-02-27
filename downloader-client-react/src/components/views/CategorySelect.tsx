import { useEffect, useRef, useState } from 'react';
import useFetch from '../../hooks/useFetch';
import Category from '../../model/Category';

export default function CategorySelect() {

  const { data, isPending } = useFetch({
		url: 'http://localhost:8082/downloaderservice/api/category',
	});

  const [categorys, setCategorys] = useState<Array<Category>>();

	const selectRef = useRef<HTMLSelectElement>(null);

	// let categorys: Array<Category> | null = null;

  /** Creamos una función que  retorna un arreglo de options */
	const createOption = () => {

    console.log("categorias: "+categorys);
    
    if (!categorys) {

			return;

		}

		categorys.forEach((category: Category) => {

			if (!selectRef.current) {

				return;

			}

			const option: HTMLOptionElement = document.createElement('option');
      
			option.text = category.categoryName;

			option.value = `${category.id}`;

			if (category.id === 1) {

				option.selected = true;

			}

			selectRef.current.add(option);

		});

	};

	useEffect(() => {

		if (isPending) {

			return;
		
    }

    console.log(isPending);

    // eslint-disable-next-line no-underscore-dangle
    console.log(data._embedded.categorys);

    // eslint-disable-next-line no-underscore-dangle
    setCategorys(data._embedded.categorys as Array<Category>);

		createOption();
    
    console.log("jajja");
	
// eslint-disable-next-line padded-blocks
// eslint-disable-next-line react-hooks/exhaustive-deps
}, [data]);

	return (

		<select
			ref={selectRef}
			className="form-select"
			aria-label=".form-select-lg example"
			id="category"
		/>
    
	);

}
