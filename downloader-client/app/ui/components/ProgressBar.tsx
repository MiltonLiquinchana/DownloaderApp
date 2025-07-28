"use client"

import { forwardRef, useImperativeHandle, useState } from "react"

export interface ProgressBarRef {
	updateProgress: (progress: number) => void
}

type ProgressBarProps = object

export const ProgressBar = forwardRef<ProgressBarRef, ProgressBarProps>((_props, ref) => {
	const [progress, setProgress] = useState<number>(0)

	/**
	 * Expone métodos públicos del modal para control externo
	 * Permite a componentes padre abrir/cerrar el modal usando useRef
	 */
	useImperativeHandle(ref, () => ({
		updateProgress: (progress) => setProgress(progress),
	}))

	return (
		<div className="space-y-3">
			<div className="flex justify-between items-center">
				<span className="text-sm font-semibold text-slate-700">Progreso de Descarga</span>
				<span className="text-sm font-bold text-indigo-600 bg-indigo-50 px-2 py-1 rounded-full">{progress}%</span>
			</div>
			<div className="relative">
				<div className="w-full bg-slate-200 rounded-full h-3 overflow-hidden shadow-inner">
					<div
						className="bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500 h-full rounded-full transition-all duration-500 ease-out relative overflow-hidden"
						style={{ width: `${progress}%` }}
					>
						<div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent animate-pulse" />
					</div>
				</div>
			</div>
		</div>
	)
})

ProgressBar.displayName = "ProgressBar"
