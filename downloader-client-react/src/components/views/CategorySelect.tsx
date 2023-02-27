import { forwardRef, RefObject, useEffect, useState } from 'react';
import useFetch from '../../hooks/useFetch';
import Category from '../../model/Category';

interface Props {
	changeCategoryPath: Function;
}

const CategorySelect = forwardRef<HTMLSelectElement, Props>((props, ref) => {

	const { data, isPending } = useFetch({
		url: 'http://localhost:8082/downloaderservice/api/category',
	});

	/** Estado que cambia cuando se obtiene los datos */
	const [categorys, setCategorys] = useState<Array<Category>>([]);

	/** este useEffect se ejecuta cuando el valor de isPending, data cambia */
	useEffect(() => {

		if (isPending) {

			return;

		}

		// eslint-disable-next-line no-underscore-dangle
		setCategorys(data._embedded.categorys);

	}, [data, isPending]);

	return (
		<select
			className="form-select"
			aria-label=".form-select-lg example"
			id="category"
			ref={ref}
			onChange={() => {

				const reference= ref as RefObject<HTMLSelectElement>;

				if(!reference.current){

					return;

				}

				console.log(categorys[Number(reference.current.value)-1]);

				props.changeCategoryPath(categorys[Number(reference.current.value)-1].categoryFileOutputPath);

			}}>
			{categorys.map(({ id, categoryName }: Category) => (
				<option value={id} key={id}>
					{categoryName}
				</option>
			))}
		</select>
	);

});

export default CategorySelect;
