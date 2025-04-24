import { useState } from "react";

export default function usePagination<T>(data: T[], itemsPerPage: number = 5) {
    const [currentPage, setCurrentPage] = useState(1);

    const totalPages = Math.ceil(data.length / itemsPerPage);

    const currentData = data.slice(
        (currentPage - 1) * itemsPerPage,
        currentPage * itemsPerPage
    );

    const goToPage = (page: number) => {
        if (page >= 1 && page <= totalPages) setCurrentPage(page);
    };

    const paginationRange = () => {
        const range = new Set<number>();
        range.add(currentPage);
        if (currentPage > 1) range.add(currentPage - 1);
        if (currentPage < totalPages) range.add(currentPage + 1);
        for (let i = 1; i <= 3; i++) range.add(i);
        for (let i = totalPages - 2; i <= totalPages; i++) range.add(i);
        return [...range].filter((p) => p >= 1 && p <= totalPages).sort((a, b) => a - b);
    };

    return { currentData, currentPage, goToPage, totalPages, paginationRange };
}