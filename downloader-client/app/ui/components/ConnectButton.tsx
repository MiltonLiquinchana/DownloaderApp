"use client"

import StompClientServiceImpl from "@/implement/StompClientServiceImpl"
import type StompClientService from "@/service/StompClientService"
import { useEffect, useRef } from "react"
import { Wifi } from "lucide-react"

interface ConnectButtonProps {
    handleOnProgressDownloaderState: (downloadProgress: number) => void
    onOpenModal?: () => void
}

export default function ConnectButton({ handleOnProgressDownloaderState, onOpenModal }: Readonly<ConnectButtonProps>) {
    const stompClientServiceRef = useRef<StompClientService | null>(null)

    useEffect(() => {
        stompClientServiceRef.current ??= new StompClientServiceImpl(handleOnProgressDownloaderState)
    }, [handleOnProgressDownloaderState])

    const handleConnect = () => {
        if (!stompClientServiceRef.current) {
            return
        }
        stompClientServiceRef.current.connect("stomp")

        // Abrir modal después de conectar (opcional)
        setTimeout(() => {
            onOpenModal?.()
        }, 1000)
    }

    return (
        <div className="flex items-center justify-center">
            <button
                onClick={handleConnect}
                className="group flex items-center gap-3 px-8 py-4 bg-gradient-to-r from-blue-600 to-indigo-600 text-white rounded-xl hover:from-blue-700 hover:to-indigo-700 transition-all duration-200 font-semibold shadow-lg hover:shadow-xl transform hover:-translate-y-0.5"
            >
                <Wifi className="w-5 h-5 group-hover:animate-pulse" />
                Conectar
            </button>
        </div>
    )
}
