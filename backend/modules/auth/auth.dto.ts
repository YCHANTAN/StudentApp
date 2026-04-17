export interface LoginUserDto{
    username: string;
    password: string;
}
export interface AuthResponseDto{
    user: {
        id: string;
        username: string;
        email: string;
    };
    token: string;

}