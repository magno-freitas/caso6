public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String funcao;
    private boolean ativo;
    
    public Usuario() {}
    
    public Usuario(int id, String nome, String email, String funcao, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.funcao = funcao;
        this.ativo = ativo;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFuncao() { return funcao; }
    public void setFuncao(String funcao) { this.funcao = funcao; }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", funcao='" + funcao + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}