import {  forwardRef, useEffect, useState } from 'react';
import useFetch from '../../hooks/useFetch';
import Category from '../../model/Category';

interface Props {
	changeCategoryPath: Function;
}

const CategorySelect = forwardRef<HTMLSelectElement, Props>((props, ref) => {

	/** Realizamos la consulta de categorías */
	const { data, isPending } = useFetch({
		url: 'http://localhost:8082/downloaderservice/api/category',
	});

	/** Estado que cambia cuando se obtiene los datos */
	const [categorys, setCategorys] = useState<Array<Category>>([]);

	/** Función que ejecuta la changeCategoryPath el cual cambia el valor del input del
	 * path de category */
	const handleCategoryChange = (valueSelect: number) => {

		props.changeCategoryPath(categorys[valueSelect - 1].categoryFileOutputPath);

	};

	/** este useEffect se ejecuta cuando el valor de isPending, data cambia */
	useEffect(() => {

		/** Si isPending es true no pasa nada y sale */
		if (isPending) {

			return;

		}

		/** En caso de que isPending pase se cambia el estado de category */
		// eslint-disable-next-line no-underscore-dangle
		setCategorys(data._embedded.categorys);

	}, [data, isPending]);

	return (
		<select
			className="form-select"
			aria-label=".form-select-lg example"
			id="category"
			ref={ref}
			onChange={e => {

				handleCategoryChange(Number(e.target.value));

			}}>
			{categorys.map(({ id, categoryName }: Category) => {

				if(id===1){

					handleCategoryChange(id);

				}

				return(	<option value={id} key={id}>
					{categoryName}
				</option>);

			})}
		</select>
	);

});

export default CategorySelect;
