export class User {
  public id?: number;
  public firstName?: string;
  public lastName?: string;
  public cpf?: string;
  public email?: string;
  public password?: string;
  public lastLoginDate?: Date | null = null;
  public lastLoginDateDisplay?: Date | null = null;
  public joinDate?: Date | null = null;
  public profileImageUrl?: string;
  public role?: string;
  public authorities?: [];
  public active?: boolean;
  public notLocked?: boolean;

  constructor() {
    this.firstName = '';
    this.lastName = '';
    this.cpf = '';
    this.email = '';
    this.active = false;
    this.notLocked = false;
    this.role = '';
    this.authorities = [];
    this.profileImageUrl = '';
  }
}
