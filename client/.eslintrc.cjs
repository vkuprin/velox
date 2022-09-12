module.exports = {
  env: {
    browser: true,
    es2021: true,
  },
  extends: [
    'plugin:react/recommended',
    'airbnb',
    'eslint:recommended',
    'plugin:@typescript-eslint/eslint-recommended',
    'plugin:@typescript-eslint/recommended'
  ],
  parser: '@typescript-eslint/parser',
  parserOptions: {
    ecmaFeatures: {
      jsx: true,
    },
    ecmaVersion: 'latest',
    sourceType: 'module',
  },
  plugins: [
    'react',
    '@typescript-eslint',
  ],
  rules: {
    'react/jsx-filename-extension': [2, { 'extensions': ['.js', '.jsx', '.ts', '.tsx'] }],
    'react/function-component-definition': [2, { "namedComponents": "arrow-function" }],
    'react/jsx-uses-react': 0,
    'react/react-in-jsx-scope': 0,
    'no-restricted-exports': 0,
    'react/require-default-props': 0,
    'import/extensions': 0,
    'react/jsx-props-no-spreading': 0,
    'no-plusplus': 0,
    'import/no-unresolved': 0,
    'react/jsx-no-constructed-context-values': 0,
    'consistent-return': 0,
    'no-return-await': 0,
    'no-use-before-define': 0,
    'react/no-unstable-nested-components': 0,
    '@typescript-eslint/ban-ts-comment': 0,
    '@typescript-eslint/no-empty-function': 0,
    'implicit-arrow-linebreak': 0,
  },
};
