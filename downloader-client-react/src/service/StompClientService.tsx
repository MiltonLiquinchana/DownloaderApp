export default interface StompClientService {
	connect(myUser: string): void;
	disconnect(): void;
	onMessage(myUser: string): void;
}
