import { ChangeEvent, useRef } from 'react';
import '../css/DownloaderFileInfo.css';
import CategorySelect from './views/CategorySelect';
import SearchSelect from './views/SearchSelect';

export default function DownloaderFileInfoComponent() {

	const refer = useRef<HTMLDivElement>(null);
	const categorySelectRef = useRef<HTMLSelectElement>(null);
	const inputCategoryPath = useRef<HTMLInputElement>(null);

	const clicleable = () => {

		console.log(refer.current?.getAttribute('data-value'));

	};

	const categoryFunction = (path: string) => {

		if (!inputCategoryPath.current) {

			return;

		}

		inputCategoryPath.current.value = path;

	};

	return (
		<>
			{/* Button trigger modal */}
			<button
				type="button"
				className="btn btn-primary"
				data-bs-toggle="modal"
				data-bs-target="#staticBackdrop">
				Launch static backdrop modal
			</button>

			{/* Modal */}
			<div
				className="modal fade"
				id="staticBackdrop"
				data-bs-backdrop="static"
				data-bs-keyboard="false"
				tabIndex={-1}
				aria-labelledby="staticBackdropLabel"
				aria-hidden="true">
				<div className="modal-dialog modal-dialog-centered modal-lg">
					<div className="modal-content">
						<div className="modal-header">
							<h1 className="modal-title fs-5" id="staticBackdropLabel">
								Downloader File Info
							</h1>
							<button
								type="button"
								className="btn-close"
								data-bs-dismiss="modal"
								aria-label="Close"
							/>
						</div>
						<div className="modal-body">
							<form>
								<div className="mb-4 row">
									<label htmlFor="url" className="col-sm-2 col-form-label">
										URL
									</label>
									<div className="col-sm-10">
										<input
											type="text"
											className="form-control"
											id="url"
										/>
									</div>
								</div>

								<div className="mb-4 row">
									<label htmlFor="category" className="col-sm-2 col-form-label">
										Category
									</label>
									<div className="col-auto">
										<div className="row g-2">
											<div className="col-auto">
												<CategorySelect
													changeCategoryPath={categoryFunction}
													ref={categorySelectRef}
												/>
											</div>
											<div className="col-auto">
												<button type="button" className="btn btn-outline-info">
													+
												</button>
											</div>
										</div>
									</div>
								</div>

								<div className="mb-4 row">
									<label
										htmlFor="inputPassword"
										className="col-sm-2 col-form-label">
										Save As
									</label>
									<div className="col-sm-10">
										<div className="row g-2">
											<div className="col-sm-10">
												<div className="mb-2 row">
													<SearchSelect ref={refer} />
												</div>
												<div className="row">
													<div className="mb-1 col-sm-12">
														<div className="form-check">
															<input
																className="form-check-input"
																type="checkbox"
																id="flexCheckDefault"
															/>
															<label
																className="form-check-label"
																htmlFor="flexCheckDefault">
																Remember this path for General category
															</label>
														</div>
													</div>

													<div className="col-sm-12">
														<input
															ref={inputCategoryPath}
															type="text"
															className="form-control "
															id="inputPassword"
															value="\Downloads"
															disabled
															readOnly
														/>
													</div>
												</div>
											</div>
											<div className="col-sm-2">
												<button
													type="button"
													className="btn btn-outline-info"
													onClick={() => {

														clicleable();

													}}>
													...
												</button>
											</div>
										</div>
									</div>
								</div>
								<div className="mb-4 row">
									<label
										htmlFor="inputPassword"
										className="col-sm-2 col-form-label">
										Description
									</label>
									<div className="col-sm-10">
										<input
											type="text"
											className="form-control"
											id="inputPassword"
										/>
									</div>
								</div>
							</form>
						</div>
						<div className="modal-footer">
							<div
								className="btn-group"
								role="group"
								aria-label="Basic mixed styles example">
								<button
									type="button"
									className="btn btn-danger"
									data-bs-dismiss="modal">
									Download Later
								</button>
								<button type="button" className="btn btn-warning">
									Start Download
								</button>
								<button type="button" className="btn btn-success">
									Cancel
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</>
	);

}
