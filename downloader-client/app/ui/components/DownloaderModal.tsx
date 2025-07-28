"use client"

import { forwardRef, useImperativeHandle, useState } from "react"
import { X, Plus, Download } from "lucide-react"

export interface DownloaderModalRef {
    openModal: () => void
    closeModal: () => void
}

interface DownloaderModalProps {
    onDownloadLater?: (data: DownloadData) => void
    onStartDownload?: (data: DownloadData) => void
}

interface DownloadData {
    url: string
    category: string
    saveAs: string
    description: string
}

export const DownloaderModal = forwardRef<DownloaderModalRef, DownloaderModalProps>(
    ({ onDownloadLater, onStartDownload }, ref) => {
        const [isOpen, setIsOpen] = useState(false)
        const [formData, setFormData] = useState<DownloadData>({
            url: "",
            category: "General",
            saveAs: "",
            description: "",
        })

        useImperativeHandle(ref, () => ({
            openModal: () => setIsOpen(true),
            closeModal: () => setIsOpen(false),
        }))

        const handleClose = () => {
            setIsOpen(false)
        }

        const handleInputChange = (field: keyof DownloadData, value: string) => {
            setFormData((prev) => ({ ...prev, [field]: value }))
        }

        const handleDownloadLater = () => {
            onDownloadLater?.(formData)
            handleClose()
        }

        const handleStartDownload = () => {
            onStartDownload?.(formData)
            handleClose()
        }

        if (!isOpen) return null

        return (
            <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50">
                <div className="bg-white rounded-2xl shadow-2xl w-full max-w-xl mx-4 border border-slate-200">
                    {/* Header */}
                    <div className="flex items-center justify-between p-6 border-b border-slate-200 bg-gradient-to-r from-slate-50 to-slate-100 rounded-t-2xl">
                        <div className="flex items-center gap-3">
                            <div className="p-2 bg-indigo-100 rounded-lg">
                                <Download className="w-5 h-5 text-indigo-600" />
                            </div>
                            <h2 className="text-xl font-bold text-slate-800">Información de Descarga</h2>
                        </div>
                        <button
                            onClick={handleClose}
                            className="p-2 text-slate-400 hover:text-slate-600 hover:bg-slate-200 rounded-lg transition-colors"
                        >
                            <X size={20} />
                        </button>
                    </div>

                    {/* Content */}
                    <div className="p-6 space-y-6">
                        {/* URL Field */}
                        <div className="flex flex-col sm:flex-row sm:items-center gap-3 sm:gap-4">
                            <label className="text-sm font-semibold text-slate-700 sm:w-24 flex-shrink-0" htmlFor="url">URL</label>
                            <input
                                id="url"
                                type="text"
                                value={formData.url}
                                onChange={(e) => handleInputChange("url", e.target.value)}
                                className="flex-1 px-4 py-3 border border-slate-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all bg-slate-50 hover:bg-white"
                                placeholder="https://ejemplo.com/archivo.zip"
                            />
                        </div>

                        {/* Category Field */}
                        <div className="flex flex-col sm:flex-row sm:items-center gap-3 sm:gap-4">
                            <label className="text-sm font-semibold text-slate-700 sm:w-24 flex-shrink-0" htmlFor="category">Categoría</label>
                            <div className="flex items-center gap-2 flex-1">
                                <select id="category"
                                    value={formData.category}
                                    onChange={(e) => handleInputChange("category", e.target.value)}
                                    className="flex-1 px-4 py-3 border border-slate-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all bg-slate-50 hover:bg-white"
                                >
                                    <option value="General">General</option>
                                    <option value="Documents">Documentos</option>
                                    <option value="Images">Imágenes</option>
                                    <option value="Videos">Videos</option>
                                    <option value="Music">Música</option>
                                </select>
                                <button className="p-3 text-emerald-600 hover:bg-emerald-50 rounded-xl transition-colors border border-emerald-200 hover:border-emerald-300">
                                    <Plus size={16} />
                                </button>
                            </div>
                        </div>

                        {/* Save As Field */}
                        <div className="flex flex-col sm:flex-row sm:items-start gap-3 sm:gap-4">
                            <label className="text-sm font-semibold text-slate-700 sm:w-24 flex-shrink-0 sm:pt-3" htmlFor="save-as">Guardar en</label>
                            <div className="flex-1">
                                <div className="flex items-center gap-2">
                                    <input
                                        id="save-as"
                                        type="text"
                                        value={formData.saveAs}
                                        onChange={(e) => handleInputChange("saveAs", e.target.value)}
                                        className="flex-1 px-4 py-3 border border-slate-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all bg-slate-50 hover:bg-white"
                                        placeholder="Buscar carpeta..."
                                    />
                                    <button className="p-3 text-emerald-600 hover:bg-emerald-50 rounded-xl transition-colors border border-emerald-200 hover:border-emerald-300">
                                        <Plus size={16} />
                                    </button>
                                </div>

                                {/* Path suggestion with checkbox and disabled input */}
                                <div className="mt-4 space-y-3">
                                    <div className="flex items-center gap-3">
                                        <input
                                            type="checkbox"
                                            id="rememberPath"
                                            checked={true}
                                            className="w-4 h-4 text-indigo-600 bg-slate-100 border-slate-300 rounded focus:ring-indigo-500 focus:ring-2"
                                            readOnly
                                        />
                                        <label htmlFor="rememberPath" className="text-sm text-slate-600">
                                            Recordar esta ruta para la categoría {formData.category}
                                        </label>
                                    </div>
                                    <input
                                        type="text"
                                        value="\Downloads"
                                        disabled
                                        className="w-full px-4 py-3 bg-slate-100 border border-slate-200 rounded-xl text-slate-500 text-sm font-mono cursor-not-allowed"
                                        readOnly
                                    />
                                </div>
                            </div>
                        </div>

                        {/* Description Field */}
                        <div className="flex flex-col sm:flex-row sm:items-start gap-3 sm:gap-4">
                            <label className="text-sm font-semibold text-slate-700 sm:w-24 flex-shrink-0 sm:pt-3" htmlFor="description">Descripción</label>
                            <textarea
                                id="description"
                                value={formData.description}
                                onChange={(e) => handleInputChange("description", e.target.value)}
                                rows={1}
                                className="flex-1 px-4 py-3 border border-slate-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent resize-none min-h-[48px] transition-all bg-slate-50 hover:bg-white"
                                placeholder="Descripción opcional..."
                            />
                        </div>
                    </div>

                    {/* Footer */}
                    <div className="flex flex-col sm:flex-row items-stretch sm:items-center justify-end gap-3 p-6 border-t border-slate-200 bg-slate-50 rounded-b-2xl">
                        <button
                            onClick={handleDownloadLater}
                            className="px-6 py-3 bg-gradient-to-r from-amber-500 to-orange-500 text-white rounded-xl hover:from-amber-600 hover:to-orange-600 transition-all duration-200 font-semibold shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 order-3 sm:order-1"
                        >
                            Descargar Después
                        </button>
                        <button
                            onClick={handleStartDownload}
                            className="px-6 py-3 bg-gradient-to-r from-emerald-500 to-teal-600 text-white rounded-xl hover:from-emerald-600 hover:to-teal-700 transition-all duration-200 font-semibold shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 order-1 sm:order-2"
                        >
                            Iniciar Descarga
                        </button>
                        <button
                            onClick={handleClose}
                            className="px-6 py-3 bg-gradient-to-r from-slate-500 to-slate-600 text-white rounded-xl hover:from-slate-600 hover:to-slate-700 transition-all duration-200 font-semibold shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 order-2 sm:order-3"
                        >
                            Cancelar
                        </button>
                    </div>
                </div>
            </div>
        )
    },
)

DownloaderModal.displayName = "DownloaderModal"
