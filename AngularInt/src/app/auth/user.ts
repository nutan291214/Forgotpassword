export class User {
    constructor(public id: number, 
                public name: string,
                public email: string,
                // public imageUrl: string,
                public emailVerified: boolean) {}
  }