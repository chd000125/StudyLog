import React, { useState, useEffect } from 'react';
import '../../style/board/SearchBar.css';
import {
    autocompletePostTitle,
    searchPosts,
    searchComments,
    searchBoards
} from '../../api/boardApi'; // ✅ axios 대신 boardApi 사용

function SearchBar() {
    const [keyword, setKeyword] = useState('');
    const [suggestions, setSuggestions] = useState([]);
    const [postResults, setPostResults] = useState([]);
    const [commentResults, setCommentResults] = useState([]);
    const [boardResults, setBoardResults] = useState([]);

    useEffect(() => {
        const fetchSuggestions = async () => {
            if (!keyword.trim()) {
                setSuggestions([]);
                return;
            }

            try {
                const res = await autocompletePostTitle(keyword);
                setSuggestions(Array.isArray(res.data) ? res.data : []);
            } catch (err) {
                console.error('자동완성 실패:', err);
            }
        };

        const delay = setTimeout(() => fetchSuggestions(), 300);
        return () => clearTimeout(delay);
    }, [keyword]);

    const handleSearch = async () => {
        if (!keyword.trim()) return;

        try {
            const [posts, comments, boards] = await Promise.all([
                searchPosts(keyword, 0, 5),
                searchComments(keyword, 0, 5),
                searchBoards(keyword, 0, 5),
            ]);

            setPostResults(posts.data.content || []);
            setCommentResults(comments.data.content || []);
            setBoardResults(boards.data.content || []);
            setSuggestions([]);
        } catch (error) {
            console.error('검색 실패:', error);
        }
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter') {
            handleSearch();
        }
    };

    const handleSuggestionClick = (text) => {
        setKeyword(text);
        handleSearch();
    };

    return (
        <div className="search-container">
            <input
                type="text"
                placeholder="검색어를 입력하세요"
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
                onKeyDown={handleKeyPress}
            />
            <button onClick={handleSearch}>검색</button>

            {keyword.trim() && (
                <ul className="autocomplete-list">
                    {suggestions.length > 0 ? (
                        suggestions.map((s, index) => (
                            <li key={index} onMouseDown={() => handleSuggestionClick(s)}>
                                {s}
                            </li>
                        ))
                    ) : (
                        <li className="no-results">검색 결과가 없습니다.</li>
                    )}
                </ul>
            )}

            {postResults.length > 0 && (
                <div className="search-results">
                    <h3>게시글 검색 결과</h3>
                    {postResults.map((post) => (
                        <div key={post.id} className="search-card">
                            <h4>{post.title}</h4>
                            <p>{post.content}</p>
                            <div className="meta">
                                작성자: {post.nickname} | {new Date(post.createdAt).toLocaleDateString()}
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {commentResults.length > 0 && (
                <div className="search-results">
                    <h3>댓글 검색 결과</h3>
                    {commentResults.map((comment) => (
                        <div key={comment.id} className="search-card">
                            <p>{comment.content}</p>
                            <div className="meta">
                                작성자: {comment.nickname} | {new Date(comment.createdAt).toLocaleDateString()}
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {boardResults.length > 0 && (
                <div className="search-results">
                    <h3>보드 검색 결과</h3>
                    {boardResults.map((board) => (
                        <div key={board.id} className="search-card">
                            <p>카테고리명: {board.category}</p>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default SearchBar;
