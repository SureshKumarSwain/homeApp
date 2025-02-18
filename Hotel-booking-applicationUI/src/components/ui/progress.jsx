const Progress = ({ value }) => {
    return (
        <div className="w-full bg-gray-300 rounded-full h-4 overflow-hidden">
            <div
                className="h-full bg-green-500 transition-all duration-500"
                style={{ width: `${value}%` }}
            ></div>
        </div>
    );
};

export { Progress };
