import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../../style/board/PostList.css';
import {
    fetchPostsByBoardWithTags,
    fetchAllPostsByTags,
} from '../../api/boardApi'; // ✅ axios 대신 boardApi 함수 사용

// 시간 차이 계산 함수
const timeAgo = (dateString) => {
    const now = new Date();
    const date = new Date(dateString);
    const diffInSeconds = Math.floor((now - date) / 1000);

    const diffInMinutes = Math.floor(diffInSeconds / 60);
    const diffInHours = Math.floor(diffInSeconds / 3600);
    const diffInDays = Math.floor(diffInSeconds / 86400);

    if (diffInMinutes < 1) return '방금 전';
    if (diffInMinutes < 60) return `${diffInMinutes}분 전`;
    if (diffInHours < 24) return `${diffInHours}시간 전`;
    return `${diffInDays}일 전`;
};

function PostList({ boardId, tags }) {
    const [posts, setPosts] = useState([]);
    const [pageInfo, setPageInfo] = useState({ number: 0, totalPages: 0 });
    const [currentPage, setCurrentPage] = useState(0);
    const navigate = useNavigate();
    const pageSize = 10;

    const fetchPosts = async (page = 0) => {
        try {
            const response = boardId
                ? await fetchPostsByBoardWithTags(boardId, tags, page, pageSize)
                : await fetchAllPostsByTags(tags, page, pageSize);

            setPosts(response.data.content);
            setPageInfo({
                number: response.data.number,
                totalPages: response.data.totalPages,
            });
        } catch (error) {
            console.error('게시글 목록 불러오기 실패:', error);
        }
    };

    useEffect(() => {
        setCurrentPage(0);
    }, [boardId, tags]);

    useEffect(() => {
        fetchPosts(currentPage);
    }, [boardId, tags, currentPage]);

    const handlePostClick = (postId) => navigate(`/posts/${postId}`);
    const handlePageChange = (pageNum) => setCurrentPage(pageNum);
    const handleNextPage = () => {
        if (currentPage < pageInfo.totalPages - 1) setCurrentPage(currentPage + 1);
    };
    const handlePreviousPage = () => {
        if (currentPage > 0) setCurrentPage(currentPage - 1);
    };

    const getPageRange = () => {
        const start = Math.floor(currentPage / 10) * 10;
        const end = Math.min(start + 9, pageInfo.totalPages - 1);
        return { start, end };
    };

    const { start, end } = getPageRange();

    return (
        <div className="post-list-container">
            {posts.length === 0 ? (
                <div className="no-posts">게시글이 없습니다.</div>
            ) : (
                posts.map((post) => (
                    <div
                        key={post.id}
                        className="post-row"
                        onClick={() => handlePostClick(post.id)}
                    >
                        <div className="post-title">{post.title}</div>
                        <div className="post-preview">
                            {post.content.length > 100
                                ? post.content.slice(0, 100) + '...'
                                : post.content}
                        </div>
                        {post.tags?.length > 0 && (
                            <div className="post-tags">
                                {post.tags.map((tag) => (
                                    <span key={tag.id} className="tag">{tag.name}</span>
                                ))}
                            </div>
                        )}
                        <div className="post-meta">
                            <div className="left-meta">
                                <span>{post.nickname}</span>
                                <span> • {timeAgo(post.createdAt)}</span>
                            </div>
                            <div className="right-meta">
                                <span>👁️ {post.viewCount}</span>
                                <span>💬 {post.commentCount}</span>
                            </div>
                        </div>
                    </div>
                ))
            )}

            <div className="pagination">
                <button
                    className="page-button"
                    onClick={handlePreviousPage}
                    disabled={currentPage === 0}
                >
                    &lt; 이전
                </button>

                {Array.from({ length: end - start + 1 }, (_, idx) => start + idx).map((pageNum) => (
                    <button
                        key={pageNum}
                        className={`page-button ${currentPage === pageNum ? 'active' : ''}`}
                        onClick={() => handlePageChange(pageNum)}
                    >
                        {pageNum + 1}
                    </button>
                ))}

                <button
                    className="page-button"
                    onClick={handleNextPage}
                    disabled={currentPage === pageInfo.totalPages - 1}
                >
                    다음 &gt;
                </button>
            </div>
        </div>
    );
}

export default PostList;
