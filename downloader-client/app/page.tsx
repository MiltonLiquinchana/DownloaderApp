"use client"

import { useRef } from "react"

import ConnectButton from "./ui/components/ConnectButton"
import { ProgressBar, ProgressBarRef } from "./ui/components/ProgressBar"
import { DownloaderModal, DownloaderModalRef } from "./ui/components/DownloaderModal"

export default function Home() {
  const progressBarRef = useRef<ProgressBarRef>(null)
  const modalRef = useRef<DownloaderModalRef>(null)

  const progressDownload = (progress: number) => {
    console.log("progreso de descarga: " + progress + "%")
    progressBarRef.current?.updateProgress(progress)
  }

  const handleOpenModal = () => {
    modalRef.current?.openModal()
  }

  const handleDownloadLater = (data: unknown) => {
    console.log("Download Later:", data)
  }

  const handleStartDownload = (data: unknown) => {
    console.log("Start Download:", data)
    // Aquí podrías iniciar el progreso de descarga
    progressBarRef.current?.updateProgress(0)
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50 to-indigo-100 py-12 px-4">
      <div className="max-w-lg mx-auto">
        {/* Header */}
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-slate-800 mb-2">STOMP Connection</h1>
          <p className="text-slate-600">Gestiona tu conexión y descargas de forma sencilla</p>
        </div>

        {/* Main Card */}
        <div className="bg-white/80 backdrop-blur-sm rounded-2xl shadow-xl border border-white/20 p-8">
          <div className="space-y-8">
            <ConnectButton handleOnProgressDownloaderState={progressDownload} />
            <ProgressBar ref={progressBarRef} />

            <div className="pt-4 border-t border-slate-200">
              <div className="text-center">
                <button
                  onClick={handleOpenModal}
                  className="px-6 py-3 bg-gradient-to-r from-emerald-500 to-teal-600 text-white rounded-xl hover:from-emerald-600 hover:to-teal-700 transition-all duration-200 font-medium shadow-lg hover:shadow-xl transform hover:-translate-y-0.5"
                >
                  Abrir Modal de Descarga
                </button>
              </div>
            </div>
          </div>
        </div>

        {/* Footer */}
        <div className="text-center mt-8">
          <p className="text-slate-500 text-sm">Powered by STOMP Protocol</p>
        </div>
      </div>

      <DownloaderModal ref={modalRef} onDownloadLater={handleDownloadLater} onStartDownload={handleStartDownload} />
    </div>
  )
}
