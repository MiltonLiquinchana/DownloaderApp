import { ChangeEvent, forwardRef, MouseEvent, RefObject, useRef } from 'react';

interface Props {}

const SearchSelect = forwardRef<HTMLDivElement, Props>((props, ref) => {

	/** Crea una referencia al div contenedor, pero cmo necesitamos usar la referencia hacia
	 * el div desde DownloaderFileInfoComponent recibimos la referencia desde
	 * DownloaderFileInfoComponent, y la convertimos a la referencia del tipo que
	 * necesitamos, para poder usarla desde aquí también */
	const searchSelected = ref as RefObject<HTMLDivElement>;

	/** Crea una referencia de la lista de elementos */
	const listOptionRef = useRef<HTMLUListElement>(null);

	/** Función que aplica un filtro de búsqueda en la lista */
	const searchOnList = (e: ChangeEvent<HTMLInputElement>) => {

		const regex = new RegExp(e.target.value, 'i');

		if (!listOptionRef.current) {

			return;

		}

		/** Seleccionamos todos los li */
		const items = listOptionRef.current.querySelectorAll('li');

		/** Recorremos la lista de li */
		items.forEach((item: HTMLLIElement) => {

			// Validamos que el elemento tenga texto
			if (!item.textContent) {

				return;

			}

			/** Valida si hay coincidencias en el texto */
			if (!item.textContent.match(regex)) {

				item.setAttribute('hidden', 'true');

			} else {

				/** De no haber coincidencias quita la propiedad de ocultar */
				item.removeAttribute('hidden');

			}

		});

	};

	/** Función que cambia el valor seleccionado, dependiendo de la lista */
	const itemSelected = (e: MouseEvent<HTMLButtonElement>) => {

		if (!searchSelected.current) {

			return;

		}

		const button = e.target as HTMLButtonElement;

		searchSelected.current.setAttribute('data-value', button.value);

	};

	return (
		<div ref={searchSelected} className="dropdown">
			<input
				className="form-control"
				type="text"
				data-bs-toggle="dropdown"
				placeholder="Type to search..."
				onChange={e => {

					searchOnList(e);

				}}
			/>

			<ul ref={listOptionRef} className="dropdown-menu">
				<li>
					<button
						className="dropdown-item"
						type="button"
						value={1}
						onClick={e => {

							itemSelected(e);

						}}>
							Action
					</button>
				</li>
				<li>
					<button
						className="dropdown-item"
						type="button"
						value={2}
						onClick={e => {

							itemSelected(e);

						}}>
							Another Action
					</button>
				</li>
				<li>
					<button
						className="dropdown-item"
						type="button"
						value={3}
						onClick={e => {

							itemSelected(e);

						}}>
							Something else here
					</button>
				</li>
			</ul>
		</div>
	);

});

export default SearchSelect;
